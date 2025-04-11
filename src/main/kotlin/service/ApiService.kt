package com.diploma.server.service

import com.diploma.server.repository.ItemRepository
import com.diploma.server.repository.ShopRepository
import com.diploma.server.repository.UserRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter

@Service
class ApiService (
    private val itemRepository: ItemRepository,
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository
){

    fun generateReport(): File{
        val items = itemRepository.findAll()

        val file = File(".reports/tmp.cvs")
        FileWriter(file).use { writer ->
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Name", "Payment"))

            items.forEach { item ->
                csvPrinter.printRecord(item.name, item.quantity, item.price, item.barcode)
            }

            // Пустая строка как разделитель
            csvPrinter.println()
            // Статистика в конце
            csvPrinter.printRecord("Total Items", items.size)

            csvPrinter.flush()
        }
        return file
    }
}