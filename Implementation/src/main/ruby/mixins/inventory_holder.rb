require '../main'
require '../namespace'

module RemoteEntities
	module EntityMixins
		module InventoryHolder
			class << self
				java_signature 'org.bukkit.Inventory getInventory()'
				def inventory
					if self.entity.is_a?(RemoteEntities::RemoteEntityHandle)
						self.entity.inventory
					elsif self.features.has_feature RemoteEntities::Features::InventoryFeature.java_class
						self.features.get_feature(RemoteEntities::Features::InventoryFeature.java_class).inventory
					end
				end
			end
		end
	end
end