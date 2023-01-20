package com.shulha;

import com.shulha.service.ShopService;
import com.shulha.types.DeviceTypes;
import com.shulha.types.SaleTypes;

public class Main {
    public static void main(String[] args) {
        final ShopService shopService = ShopService.getInstance();

        for (int i = 0; i < 15; i++) {
            shopService.getRandomInvoice("csv/goods.csv");
        }

        shopService.getNumberOfSoldGoodsByCategory(DeviceTypes.TELEPHONE);
        shopService.getInvoiceWithTheLowestCost();
        shopService.getTotalCostOfAllInvoices();
        shopService.getInvoiceNumberByCategory(SaleTypes.RETAIL);
        shopService.getInvoicesThatContainOnlyOneType();
        shopService.getFirstInvoices(3);
        shopService.getLowAgeInvoices();
        shopService.getSortedInvoiceArray();
    }
}
