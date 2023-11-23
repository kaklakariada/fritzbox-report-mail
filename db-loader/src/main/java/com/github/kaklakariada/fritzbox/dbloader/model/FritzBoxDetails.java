package com.github.kaklakariada.fritzbox.dbloader.model;

import de.siegmar.fastcsv.reader.NamedCsvRow;

public record FritzBoxDetails(String productName, String readableName) {
    public static FritzBoxDetails fromCsv(final NamedCsvRow row) {
        return new FritzBoxDetails(row.getField("PRODUCT_NAME"), row.getField("READABLE_NAME"));
    }
}
