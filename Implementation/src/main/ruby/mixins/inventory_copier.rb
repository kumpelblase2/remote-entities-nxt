require '../main'
require '../namespace'

module RemoteEntities
	module EntityMixins
		module InventoryCopier
			java_signature 'void copyInventory(org.bukkit.entity.Player)'
			def copy_inventory_player_default(in_player)
				self.copy_inventory_player in_player
			end

			java_signature 'void copyInventory(org.bukkit.entity.Player, boolean)'
			def copy_inventory_player(in_player, in_ignore_armor = false)
				self.copy_inventory(in_player.inventory)
				equip = self.bukkit_entity.equipment
				unless in_ignore_armor
					equip.armor_contents = in_player.inventory.armor_contents
				end

				if self.inventory.is_a?(CB::Inventory::CraftInventoryPlayer)
					self.inventory.held_item_slot = in_player.inventory.held_item_slot
				else
					equip.item_in_hand = in_player.item_in_hand # FIXME does this reference the weapon of the player or just a copy?
				end
			end

			java_signature 'void copyInventory(org.bukkit.inventory.Inventory)'
			def copy_inventory(in_inv)
				if self.inventory
					self.inventory.contents = in_inv.contents
				end
			end
		end
	end
end