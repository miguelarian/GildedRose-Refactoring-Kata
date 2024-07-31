package com.gildedrose;

class GildedRose {
    public static final String AGED_BRIE = "Aged Brie";
    public static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS = "Sulfuras, Hand of Ragnaros";

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            if (isStandardItem(item)) {
                decreaseQuality(item, 1);
            } else {
                increaseQuality(item);
            }

            decreaseSellIn(item, 1);

            if (isSellInExpired(item)) {
                updateQualityOfExpiredSellInItem(item);
            }
        }
    }

    private static void increaseQuality(Item item) {
        if (item.quality < 50) {
            increaseQuality(item, 1);

            if (isBackstagePass(item)) {
                if (item.sellIn < 11 && item.quality < 50) {
                    increaseQuality(item, 1);
                }

                if (item.sellIn < 6 && item.quality < 50) {
                    increaseQuality(item, 1);
                }
            }
        }
    }

    private static void updateQualityOfExpiredSellInItem(Item item) {
        if (!isAgedBrie(item)) {
            if (!isBackstagePass(item)) {
                if (!isSulfuras(item)) {
                    decreaseQuality(item, 1);
                }
            } else {
                item.quality = item.quality - item.quality;
            }
        } else {
            if (item.quality < 50) {
                increaseQuality(item, 1);
            }
        }
    }

    private static boolean isSellInExpired(Item item) {
        return item.sellIn < 0;
    }

    private static void increaseQuality(Item item, int increment) {
        item.quality = item.quality + increment;
    }

    private static void decreaseSellIn(Item item, int decrement) {
        if (isSulfuras(item)) {
            return;
        }

        item.sellIn = item.sellIn - decrement;
    }

    private static void decreaseQuality(Item item, int decrement) {
        if (item.quality > 0) {
            item.quality = item.quality - decrement;
        }
    }

    private static boolean isStandardItem(Item item) {
        return !isAgedBrie(item) && !isBackstagePass(item) && !isSulfuras(item);
    }

    private static boolean isSulfuras(Item item) {
        return item.name.equals(SULFURAS);
    }

    private static boolean isBackstagePass(Item item) {
        return item.name.equals(BACKSTAGE_PASS);
    }

    private static boolean isAgedBrie(Item item) {
        return item.name.equals(AGED_BRIE);
    }
}
