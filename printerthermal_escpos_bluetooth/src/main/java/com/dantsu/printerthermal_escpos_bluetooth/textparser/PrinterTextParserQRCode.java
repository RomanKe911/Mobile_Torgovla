package com.dantsu.printerthermal_escpos_bluetooth.textparser;

import com.dantsu.printerthermal_escpos_bluetooth.Printer;
import com.dantsu.printerthermal_escpos_bluetooth.PrinterCommands;

import java.util.Hashtable;

public class PrinterTextParserQRCode extends PrinterTextParserImg {

    private static byte[] initConstructor(PrinterTextParserColumn printerTextParserColumn, Hashtable<String, String> qrCodeAttributes, String data) {
        Printer printer = printerTextParserColumn.getLine().getTextParser().getPrinter();
        data = data.trim();

        int size = printer.mmToPx(20f);
        try {
            if (qrCodeAttributes.containsKey(PrinterTextParser.ATTR_QRCODE_SIZE)) {
                size = printer.mmToPx(Float.parseFloat(qrCodeAttributes.get(PrinterTextParser.ATTR_QRCODE_SIZE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PrinterCommands.QRCodeDataToBytes(data, size);
    }

    public PrinterTextParserQRCode(PrinterTextParserColumn printerTextParserColumn, String textAlign, Hashtable<String, String> qrCodeAttributes, String data) {
        super(
                printerTextParserColumn,
                textAlign,
                PrinterTextParserQRCode.initConstructor(printerTextParserColumn, qrCodeAttributes, data)
        );
    }
}
