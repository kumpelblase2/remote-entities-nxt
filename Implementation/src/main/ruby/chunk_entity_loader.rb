require 'java'
require 'namespace'
require 'base'

module RemoteEntities
	class ChunkEntityLoader
		include Java::org.bukkit.event.Listener

		attr_accessor :manager
		attr_accessor :to_spawn

		def initialize(in_manager)
			self.manager = in_manager
			self.to_spawn = java.util.HashSet.new
		end

		java_signature 'void onChunkLoad(org.bukkit.event.world.ChunkLoadEvent)'
		def on_chunk_load(in_event)
			chunk = in_event.chunk
			self.manager.all_entities.each { |entity|
				unless entity.is_spawned
					if entity.bukkit_entity.location.chunk.equal?(chunk)
						RemoteEntities::Helpers::WorldUtilities.inst.update_entity_tracking entity, chunk
					end
				end
			}
		end

		java_signature 'void onChunkUnload(org.bukkit.event.world.ChunkUnloadEvent)'
		def on_chunk_unload(in_event)
			chunk = in_event.chunk
			chunk.entities.each { |entity|
				if entity.is_a?(Java::org.bukkit.entity.LivingEntity)
					remote = Java::de.kumpelblase2.remoteentities.RemoteEntities.remote_entity_from_entity entity
					if remote and remote.is_spawned
						self.to_spawn.add EntityLoadData.new(remote, entity.location)
						remote.despawn RemoteEntities::DespawnReason::DEATH
					end
				end
			}
		end

		def queue_spawn(in_entity, in_location, in_setup_goals = false)
			data = EntityLoadData.new(in_entity, in_location, in_setup_goals)
			if self.can_spawn_at? in_location
				self.spawn_entity(data)
				false
			else
				self.to_spawn.add data
				true
			end
		end

		def spawn_entity(in_data)
			in_data.entity.spawn(in_data.loc)
			if in_data.entity.is_spawned and in_data.setup_goals
				in_data.entity.setup_default_goals
			end
		end

		def unregister
			[ Java::org.bukkit.event.world.ChunkLoadEvent.handler_list, Java::org.bukkit.event.world.ChunkUnloadEvent.handler_list ].each { |list|
				list.unregister self
			}
		end

		def can_spawn_at?(in_location)
			in_location.chunk.is_loaded
		end

		class EntityLoadData
			attr_accessor :entity, :loc, :setup_goals

			def initialize(in_entity, in_loc, in_setup_goals = false)
				self.entity = in_entity
				self.loc = in_loc
				self.setup_goals = in_setup_goals
			end
		end
	end
end