package de.lbc.survivalgames.helper;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomChestManager {
    public static void fillRNG(Chest chest, int weakItems, int mediumItems, int strongItems) {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.addAll(getRandomItems(getWeakItems(), weakItems));
        items.addAll(getRandomItems(getMediumItems(), mediumItems));
        items.addAll(getRandomItems(getStrongItems(), strongItems));

        ItemStack[] content = items.toArray(new ItemStack[0]);
        chest.getInventory().setContents(content);
    }

    private static ArrayList<ItemStack> getRandomItems(List<ItemStack> items, int itemNum) {
        ArrayList<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < itemNum; i++) {
            int r = (int) (Math.random() * items.size());
            result.add(items.get(r));
        }
        return result;
    }

    private static List<ItemStack> getWeakItems() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leatherChestplate2 = new ItemBuilder(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false).build();
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD);
        ItemStack woodenSwordSharp = new ItemBuilder(Material.WOODEN_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1, false).build();
        ItemStack woodenSwordKnock = new ItemBuilder(Material.WOODEN_SWORD).addEnchantment(Enchantment.KNOCKBACK, 1, false).build();
        ItemStack bread = new ItemStack(Material.BREAD, 4);
        ItemStack bread2 = new ItemStack(Material.BREAD, 6);
        ItemStack bread3 = new ItemStack(Material.BREAD, 2);

        ItemStack[] items = new ItemStack[]{
                leatherChestplate, leatherBoots, leatherHelmet, leatherLeggings, bread, woodenSword, woodenSwordKnock, woodenSwordSharp, bread2, bread3};
        return Arrays.asList(items);
    }

    private static List<ItemStack> getMediumItems() {
        ItemStack chainChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemStack chainChestplate2 = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false).build();
        ItemStack chainChestplate3 = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false).build();
        ItemStack chainHelmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemStack chainBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemStack chainLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        ItemStack stoneSword2 = new ItemBuilder(Material.STONE_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2, false).build();
        ItemStack stoneSword3 = new ItemBuilder(Material.STONE_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2, false).addEnchantment(Enchantment.KNOCKBACK, 1, false).build();
        ItemStack stoneSword4 = new ItemBuilder(Material.STONE_SWORD).addEnchantment(Enchantment.KNOCKBACK, 2, false).build();
        ItemStack knockbackStick = new ItemBuilder(Material.STICK).addEnchantment(Enchantment.KNOCKBACK, 1, false).build();
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 6);
        ItemStack steak2 = new ItemStack(Material.COOKED_BEEF, 2);
        ItemStack steak3 = new ItemStack(Material.COOKED_BEEF, 12);

        ItemStack[] items = new ItemStack[]{chainChestplate, chainChestplate2, chainChestplate3, chainBoots, chainHelmet, chainLeggings, steak2, steak3, steak, stoneSword, stoneSword2, stoneSword3, stoneSword4, knockbackStick, chainChestplate2};
        return Arrays.asList(items);
    }

    private static List<ItemStack> getStrongItems() {
        ItemStack ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack knockbackStick = new ItemBuilder(Material.STICK).addEnchantment(Enchantment.KNOCKBACK, 2, true).build();
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemStack ironSword1 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1, false).build();
        ItemStack ironSword11 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1, false).addEnchantment(Enchantment.KNOCKBACK,1,false).build();
        ItemStack ironSword21 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2, false).addEnchantment(Enchantment.KNOCKBACK, 1, false).build();
        ItemStack ironSword2 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 2, false).build();
        ItemStack ironSword31 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 3, false).addEnchantment(Enchantment.KNOCKBACK, 1, false).build();
        ItemStack ironSword3 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 3, false).build();
        ItemStack ironSword4 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 1, false).build();
        ItemStack ironSword5 = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.KNOCKBACK, 2, false).build();
        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemStack gapple2 = new ItemStack(Material.GOLDEN_APPLE, 2);
        ItemStack gapple3 = new ItemStack(Material.GOLDEN_APPLE, 3);

        ItemStack[] items = new ItemStack[]{ironSword3,ironSword2,ironSword4,ironSword5,ironSword1, ironSword31, ironSword11, ironSword21,gapple2,gapple3,ironChestplate, ironBoots, ironHelmet, ironLeggings, gapple, knockbackStick, ironSword, diamondSword};
        return Arrays.asList(items);
    }
}
