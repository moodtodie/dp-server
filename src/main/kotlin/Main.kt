package com.diploma.server

import com.diploma.server.repository.ItemRepository
import com.diploma.server.repository.ShopRepository
import com.diploma.server.repository.UserRepository
import com.diploma.server.service.ApiService
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.security.crypto.password.PasswordEncoder
import java.io.File
import java.io.FileWriter
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    generateReport()
}

fun generateReport(): File {
    val items = ItemRepository().findAll()

    val dir = File(".reports")
    if (!dir.exists()) {
        dir.mkdirs() // создаёт директорию, если её нет
    }

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