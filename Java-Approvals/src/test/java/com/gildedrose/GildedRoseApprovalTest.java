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
            if(day > 0) {
                app.updateQuality();

            }
            output.append("-------- day " + day + " --------\n");
            output.append("name, sellIn, quality\n");
            for (Item item : items) {
                output.append(item.toString()).append("\n");
            }
            output.append("\n");
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

        assertEquals(11, items[0].quality, "The quality of Aged Brie is not 50 after 50 days");
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

        assertEquals(15, items[0].quality, "The quality twice fast when sell in expires");
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

    @Test
    public void shoud_backstagePassIncreaseQuality2_whenSellInBetween10and5() {
        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 10),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 1);

        assertEquals(12, items[0].quality, "The quality of Backstage passes should increase by 2 when expiration is between 5 and 10");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void shoud_backstagePassIncreaseQuality3_whenSellInBetween0and5() {
        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 10),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 1);

        assertEquals(13, items[0].quality, "The quality of Backstage passes should increase by 3 when expiration is between 0 and 5");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void shoud_backstagePassIncreaseQuality1_whenSellInAbove10() {
        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 11, 10),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 1);

        assertEquals(11, items[0].quality, "The quality of Backstage passes should increase by 1 when expiration is above 10");
        Approvals.verify(itemsUpdatedLogs);
    }

    @Test
    public void shoud_backstagePassSetQualityTo0_whenConcertPasses() {
        Item[] items = new Item[] {
            new Item("Backstage passes to a TAFKAL80ETC concert", 1, 10),
        };
        GildedRose app = new GildedRose(items);

        String itemsUpdatedLogs = getItemsUpdatedLogs(items, app, 2);

        assertEquals(0, items[0].quality, "The quality of Backstage passes should decrease to 0 when the concert is passed");
        Approvals.verify(itemsUpdatedLogs);
    }
}
