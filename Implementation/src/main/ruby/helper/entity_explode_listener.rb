require '../namespace'
require '../base'

module RemoteEntities
	module Helpers
		class EnderDragonExplodeHandler
			include Bukkit::Events::Listener

			java_signature 'void onExplode(org.bukkit.event.entity.EntityExplodeEvent)'
			java_annotation 'org.bukkit.event.EventHandler'
			def on_explode(in_event)
				entity = in_event.entity
				entity = entity.parent if in_event.entity.is_a?(Bukkit::Entity::ComplexEntityPart)

				if entity.is_a?(Bukkit::Entity::EnderDragon)
					re = entity.remote_entity
					in_event.cancelled = true if re and re.is_a?(RemoteEntities::Entities::RemoteEnderDragon) and re.should_destroy_blocks
				end
			end
		end
	end
end