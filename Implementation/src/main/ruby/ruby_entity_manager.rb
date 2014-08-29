require 'java'
require 'namespace'
require 'base'
require 'chunk_entity_loader'

module RemoteEntities
	class RubyEntityManager < Java::de.kumpelblase2.remoteentities.BaseEntityManager

		def setup(in_plugin)
			@chunk_loader = RemoteEntities::ChunkEntityLoader.new self
			bukkit.plugin_manager.register_events @chunk_loader, RemoteEntities.plugin
			bukkit.scheduler.schedule_sync_repeating_task in_plugin, DespawnDead.new(self), 100, 100
		end

		def teardown
			@chunk_loader.unregister
		end

		java_signature 'de.kumpelblase2.remoteentities.api.RemoteEntity createEntity(de.kumpelblase2.remoteentities.api.RemoteEntityType, org.bukkit.Location, boolean)'
		def create_entity(in_type, in_location, in_setup_goals)
			if in_type.is_named
				raise RemoteEntities::Exceptions::NoNameException.new('Tried to spawn a named entity without name')
			end

			id = self.next_free_id
			entity = RemoteEntities::Helpers::Ruby::EntityHelper.create_entity self, in_type, id
			unless entity
				return entity
			end

			self.add_remote_entity id, entity
			if in_location
				@chunk_loader.queue entity, in_location, in_setup_goals
			end

			entity
		end

		java_signature 'de.kumpelblase2.remoteentities.api.RemoteEntity createNamedEntity(de.kumpelblase2.remoteentities.api.RemoteEntityType, org.bukkit.Location, java.lang.String, boolean)'
		def create_named_entity(in_type, in_location, in_name, in_setup_goals)
			unless in_type.is_named
				entity = self.create_entity in_type, in_location, in_setup_goals
				unless entity
					return entity
				end

				entity.name = in_name
				return entity
			end

			id = self.next_free_id
			entity = RemoteEntities::Helpers::Ruby::EntityHelper.create_named_entity self, in_type, in_name, id
			unless entity
				return entity
			end

			if in_location
				@chunk_loader.queue entity, in_location, in_setup_goals
			end
		end

		java_signature 'de.kumpelblase2.remoteentities.api.RemoteEntity createRemoteEntityFromExisting(org.bukkit.entity.LivingEntity, boolean)'
		def create_remote_entity_from_existing(in_entity, in_delete)
			type = RemoteEntities::Helpers::Ruby::EntityHelper.type_for_entity in_entity
			unless type
				return nil
			end

			old_location = in_entity.location
			name = in_entity.is_a?(org.bukkit.entity.HumanEntity) ? in_entity.name : nil
			if in_delete
				in_entity.remove
			end

			if name
				self.create_entity type, old_location, true
			else
				self.create_named_entity type, old_location, name, true
			end
		end

		class DespawnDead
			include Java::java.lang.Runnable

			def initialize(in_chunk_loader)
				@chunk_loader = in_chunk_loader
			end

			def run
				it = @chunk_loader.all_entities.iterator
				while it.has_next
					entity = it.next
					if entity.bukkit_entity
						handle = entity.nms_handle
						handle.C
						if handle.dead
							if entity.despawn RemoteEntities::DespawnReason::DEATH
								it.remove
							end
						end
					else
						if @chunk_loader.should_remove_despawned_entities
							it.remove
						end
					end
				end
			end
		end
	end
end