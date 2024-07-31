package com.gildedrose;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.DiffReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UseReporter(DiffReporter.class)
public class GildedRoseApprovalTest {

    private static String getItemsUpdatedLogs(Item[] items, GildedRose app, int days) {
        StringBuilder output = new StringBuilder();

        for (int day = 0; day <= days; day++) {
            output.append("-------- day " + day + " --------\n");
            output.append("name, sellIn, quality\n");
            for (Item item : items) {
                output.append(item.toString()).append("\n");
            }
            output.append("\n");
            app.updateQuality();
        }
        String itemsUpdatedLogs = output.toString();
        return itemsUpdatedLogs;
    }

    @Test
	public void should_updateQuality_afterThirtyDays() {

        Item[] items = new Item[] {
            new Item("+5 Dexterity Vest", 10, 20),
            new Item("Aged Brie", 2, 0),
            new Item("Elixir of the Mongoose", 5, 7),
            new Item("Sulfuras, Hand of Ragnaros", 0, 80),
            new Item("Sulfuras, Hand of Ragnaros", -1, 80),
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            // this conjured item does not work properly yet
            new Item("Conjured Mana Cake", 3, 6) };

        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 30);

        Approvals.verify(itemsUpdatedLogs);
	}

    @Test
    public void should_increaseQualityOnePerDay_forAgedBrie() {
        Item[] items = new Item[] {
            new Item("Aged Brie", 50 ,1)
        };

        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 10);

        assertEquals(12, items[0].quality, "The quality of Aged Brie is not 50 after 50 days");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void should_qualityNotIncreaseAbove50_forAgedBrie() {
        Item[] items = new Item[] {
            new Item("Aged Brie", 1, 50)
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 1);

        assertEquals(50, items[0].quality, "The quality of Aged Brie should not exceed 50");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void should_qualityNeverDecreaseBelowZero_forStandardItems() {
        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 10, 1),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 3);

        assertEquals(0, items[0].quality, "The quality of item should not be less than 0");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void should_qualityDecreaseTwiceFastWhenSellInExpires_forStandardItems() {
        Item[] items = new Item[] {
            new Item("Elixir of the Mongoose", 1, 20),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 3);

        assertEquals(13, items[0].quality, "The quality twice fast when sell in expires");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void should_sulfurasQualityNeverDecrease() {
        Item[] items = new Item[] {
            new Item("Sulfuras, Hand of Ragnaros", 10, 80),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 1);

        assertEquals(80, items[0].quality, "The quality of Sulfuras should never decrease");
        Approvals.verify(itemsUpdatedLogs);
    }
}
