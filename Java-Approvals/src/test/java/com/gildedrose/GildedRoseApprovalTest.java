package com.gildedrose;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.DiffReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.Test;

@UseReporter(DiffReporter.class)
public class GildedRoseApprovalTest {

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

        StringBuilder output = new StringBuilder();

        for (int day = 0; day < 31; day++) {
            output.append("-------- day " + day + " --------\n");
            output.append("name, sellIn, quality\n");
            for (Item item : items) {
                output.append(item.toString()).append("\n");
            }
            output.append("\n");
            app.updateQuality();
        }
        Approvals.verify(output.toString());
	}
}
