require '../namespace'

module RemoteEntities
	module Helpers
		class WorldUtilities
			include 'de.kumpelblase2.remoteentities.services.WorldService'

			@@inst = WorldUtilities.new
			def self.inst
				@@inst
			end


			def inject_path(in_entity, in_path, in_speed)

			end

			def add_entity_width(in_entity, in_node)
				vec = org.bukkit.util.Vector.new in_node.x, in_node.y, in_node.z
				width = (in_entity.nms_handle.width + 1) * 0.5
				vec.add org.bukkit.util.Vector.new(width, 0, width)
				vec
			end

			def update_entity_tracking(in_entity, in_chunk)
				worldServer = in_chunk.world.nms_handle
				unless worldServer.tracker.trackedEntities.b in_entity.nms_handle.id
					worldServer.add_entity in_entity.nms_handle
				end
			end

			def send_to_player(in_player, in_packet)
				in_player.nms_handle.playerConnection.send_packet in_packet
			end
		end
	end
end