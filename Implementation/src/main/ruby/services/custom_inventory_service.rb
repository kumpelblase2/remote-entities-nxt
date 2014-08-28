require 'java'
require '../namespace'
require '../service'

module RemoteEntities
	module Services
		class CustomInventoryService
			include ServiceRegistration
			include Java::de.kumpelblase2.remoteentities.services.CustomInventoryService

			def create_inventory_holder(in_holder, in_size)
				Java::org.bukkit.craftbukkit.v1_7_R1.inventory.CraftInventoryCustom.new in_holder, in_size
			end

			def create_inventory_remote(in_remote, in_size)
				self.create_inventory_holder in_remote.nms_handle, in_size
			end

			register_for Java::de.kumpelblase2.remoteentities.services.CustomInventoryService
		end
	end
end

