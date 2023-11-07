package com.dantsu.printerthermal_escpos_bluetooth.textparser;

import com.dantsu.printerthermal_escpos_bluetooth.bluetooth.BluetoothPrinterSocketConnection;

public interface PrinterTextParserElement {
    int length();
    PrinterTextParserElement print(BluetoothPrinterSocketConnection printerSocket);
}
