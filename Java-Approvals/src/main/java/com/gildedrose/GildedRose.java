package com.gildedrose;

class GildedRose {
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String CONJURED = "Conjured Mana Cake";
    private static final int MAX_QUALITY = 50;
    private static final int MIN_QUALITY = 0;
    private static final int BACKSTAGEPASS_X3_MAX_DAYS = 6;
    private static final int BACKSTAGEPASS_X2_MAX_DAYS = 11;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            update(item);
        }
    }

    private static void update(Item item) {
        if (isConjured(item)) {
            decreaseQuality(item, 2);
        }
        else if (isAgedBrie(item)) {
            increaseQuality(item);
        }
        else if (isBackstagePass(item)) {
            increaseQuality(item);
        }
        else if (isSulfuras(item)) {
            return;
        }
        else {
            decreaseQuality(item, 1);
        }

        decreaseSellIn(item, 1);

        if (isExpired(item)) {
            updateQualityOfExpiredSellInItem(item);
        }
    }

    private static void increaseQuality(Item item) {

        if (item.quality >= MAX_QUALITY) {
            return;
        }

        increaseQuality(item, 1);

        if (isBackstagePass(item)) {
            if (item.sellIn < BACKSTAGEPASS_X2_MAX_DAYS) {
                increaseQuality(item, 1);
            }

            if (item.sellIn < BACKSTAGEPASS_X3_MAX_DAYS) {
                increaseQuality(item, 1);
            }
        }
    }

    private static void updateQualityOfExpiredSellInItem(Item item) {
        if (isAgedBrie(item)) {
            increaseQuality(item, 1);
        }
        else if (isBackstagePass(item)) {
            item.quality = MIN_QUALITY;
        }
        else if (isConjured(item)) {
            decreaseQuality(item, 2);
        }
        else if (isSulfuras(item)) {
            return;
        }
        else {
            decreaseQuality(item, 1);
        }
    }

    private static boolean isExpired(Item item) {
        return item.sellIn < 0;
    }

    private static void increaseQuality(Item item, int increment) {
        if (item.quality < MAX_QUALITY) {
            item.quality = item.quality + increment;
        }
    }

    private static void decreaseSellIn(Item item, int decrement) {
        if (isSulfuras(item)) {
            return;
        }

        item.sellIn = item.sellIn - decrement;
    }

    private static void decreaseQuality(Item item, int decrement) {
        if (item.quality > MIN_QUALITY) {
            item.quality = item.quality - decrement;
        }
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

    private static boolean isConjured(Item item) {
        return item.name.equals(CONJURED);
    }
}
