package com.diploma.server.service

import com.diploma.server.model.ActionLog
import com.diploma.server.service.enums.Actions
import com.diploma.server.service.enums.Entitys
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter
import java.sql.Timestamp
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class ApiService(
    private val itemService: ItemService,
    private val shopService: ShopService,
    private val userService: UserService,
    private val logService: ActionLogService
) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_Z")

    fun generateReport(): File {
        val items = itemService.findAll()

        val dir = File(".reports")
        if (!dir.exists()) {
            dir.mkdirs() // Creates a directory if it does not exist
        }

        val fileName = ZonedDateTime.now().format(dateTimeFormatter)

        val file = File(".reports/${fileName}.csv")
        FileWriter(file).use { writer ->
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Name", "Quan", "Prc", "Code"))

            items.forEach { item ->
                csvPrinter.printRecord(item.name, item.quantity, item.price, item.barcode)
            }

            // Empty string as a separator
            csvPrinter.println()
            // Statistics at the end
            csvPrinter.printRecord("Total Items", items.size)

            csvPrinter.flush()
        }
        return file
    }

    fun generateTurnoverReport(): TurnoverReport {
        val now = Date()
        val thirtyDaysAgo = Date(now.time - 30L * 24 * 60 * 60 * 1000)

        val allItems = itemService.findAll()
        val logsLast30Days = logService.filter(form = thirtyDaysAgo, to = now)
            .filter { it.entity == Entitys.ITEM.name && it.action == Actions.UPDATE.name }

        val logsGroupedByBarcode = logsLast30Days.groupBy { it.details }

        val barcodeToItem = allItems.associateBy { it.barcode }

        val barcodeToQuantityDiff = mutableMapOf<String, Int>()
        val userToTotalSold = mutableMapOf<String, Int>()

        for ((barcode, logs) in logsGroupedByBarcode) {
            val item = barcodeToItem[barcode] ?: continue

            // For an item, we only track changes in the quantity field
            val quantityChanges = logs.mapNotNull { log ->
                val quantityChange = extractQuantityChange(log)
                quantityChange?.let { log.createdBy.username to it }
            }

            // Counting the total number of units sold in 30 days (all quantity reductions)
            val totalSold = quantityChanges
                .filter { (_, delta) -> delta < 0 }
                .sumOf { (_, delta) -> -delta }

            if (totalSold > 0) {
                barcodeToQuantityDiff[barcode] = totalSold

                for ((username, delta) in quantityChanges) {
                    if (delta < 0) {
                        userToTotalSold[username] = userToTotalSold.getOrDefault(username, 0) + -delta
                    }
                }
            }
        }

        val soldItems = barcodeToQuantityDiff.mapNotNull { (barcode, soldQuantity) ->
            val item = barcodeToItem[barcode] ?: return@mapNotNull null
            SoldItemInfo(
                name = item.name,
                barcode = barcode,
                soldQuantity = soldQuantity
            )
        }.sortedByDescending { it.soldQuantity }

        val topSeller = userToTotalSold.maxByOrNull { it.value }?.key

        return TurnoverReport(
            generatedAt = Timestamp(System.currentTimeMillis()),
            soldItems = soldItems,
            topSeller = topSeller
        )
    }

    // log.details contains JSON like: { “barcode”: “...”, ‘oldQuantity’: 20, “newQuantity”: 15}
    fun extractQuantityChange(log: ActionLog): Int? {
        val oldRegex = """"oldQuantity"\s*:\s*(\d+)""".toRegex()
        val newRegex = """"newQuantity"\s*:\s*(\d+)""".toRegex()

        val oldQ = oldRegex.find(log.details)?.groupValues?.get(1)?.toIntOrNull()
        val newQ = newRegex.find(log.details)?.groupValues?.get(1)?.toIntOrNull()

        return if (oldQ != null && newQ != null) newQ - oldQ else null
    }
}

data class SoldItemInfo(
    val name: String,
    val barcode: String,
    val soldQuantity: Int
)

data class TurnoverReport(
    val generatedAt: Timestamp,
    val soldItems: List<SoldItemInfo>,
    val topSeller: String?
)
