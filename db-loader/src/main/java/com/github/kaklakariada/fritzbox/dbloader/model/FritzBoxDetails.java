package com.github.kaklakariada.fritzbox.dbloader.model;

import de.siegmar.fastcsv.reader.NamedCsvRecord;

public record FritzBoxDetails(String productName, String readableName) {
    public static FritzBoxDetails fromCsv(final NamedCsvRecord row) {
        return new FritzBoxDetails(row.getField("PRODUCT_NAME"), row.getField("READABLE_NAME"));
    }
}
