require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteWolfImpl < RemoteAttackingEntity
			include RemoteEntities::Entities::RemoteWolf
		end

		class RemoteWolfEntity < NMS::EntityWolf
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			override_for :method => :random_sound
			def custom_random_sound
				if self.is_alive
					self.remote_entity.get_sound RemoteEntities::EntitySound::RANDOM, :growl
				else
					if self.random.next_int(3) == 0
						if self.is_tamed && self.datawatcher.get_float(18) < 10
							self.remote_entity.get_sound RemoteEntities::EntitySound::RANDOM, :whine
						else
							self.remote_entity.get_sound RemoteEntities::EntitySound::RANDOM, :panting
						end
					else
						self.remote_entity.get_sound RemoteEntities::EntitySound::RANDOM, :bark
					end
				end
			end

			def self.default_movement_desires
				[
						# TODO
				]
			end

			def self.default_target_desires
				[
						# TODO
				]
			end
		end
	end
end
