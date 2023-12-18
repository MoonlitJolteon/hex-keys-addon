package dev.munebase.hexkeys.inventories;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class KleinInventory extends SimpleInventory
{
	public KleinInventory() {
		super(54);
	}

	public void readNbtList(NbtList nbtList) {
		int i;
		for(i = 0; i < this.size(); ++i) {
			this.setStack(i, ItemStack.EMPTY);
		}

		for(i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			if (j >= 0 && j < this.size()) {
				this.setStack(j, ItemStack.fromNbt(nbtCompound));
			}
		}

	}

	public static KleinInventory kleinInvFromNBT(NbtList nbtList) {
		KleinInventory inv = new KleinInventory();
		int i;
		for(i = 0; i < inv.size(); ++i) {
			inv.setStack(i, ItemStack.EMPTY);
		}

		for(i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			if (j >= 0 && j < inv.size()) {
				inv.setStack(j, ItemStack.fromNbt(nbtCompound));
			}
		}
		return inv;
	}

	public NbtList toNbtList() {
		NbtList nbtList = new NbtList();

		for(int i = 0; i < this.size(); ++i) {
			ItemStack itemStack = this.getStack(i);
			if (!itemStack.isEmpty()) {
				NbtCompound nbtCompound = new NbtCompound();
				nbtCompound.putByte("Slot", (byte)i);
				itemStack.writeNbt(nbtCompound);
				nbtList.add(nbtCompound);
			}
		}

		return nbtList;
	}
}
