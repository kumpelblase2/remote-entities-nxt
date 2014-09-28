require '../base'
require '../namespace'

module RemoteEntities
	module Desires
		class TempDesireTempt < NMS::PathfinderGoalTempt
			def setup(in_entity)
				@remote = in_entity
			end

			def f
				return false unless remote_desire
				remote_desire.is_tempted
			end

			def remote_desire
				@remote.mind.get_movement_desire(RemoteEntities::Desires::DesireTempt)
			end

			def self.empty
				TempDesireTempt.new nil, 0, NMS::Items.SEEDS, false
			end
		end

		class TempDesireSit < NMS::PathfinderGoalSit
			def setup(in_entity)
				@remote = in_entity
			end

			java_signature 'void setSitting(boolean)'
			def sitting=(in_flag)
				remote_desire.sit = in_flag if remote_desire
			end

			def remote_desire
				@remote.mind.get_movement_desire(RemoteEntities::Desires::DesireSit)
			end

			def self.empty
				TempDesireSit.new nil
			end
		end
	end

	module Entities
		class RemoteOceloteImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteOcelote
		end

		class RemoteOceloteEntity < NMS::EntityOcelot
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			def on_create
				tempSit = RemoteEntities::Desires::TempDesireSit.empty
				tempSit.setup self.remote_entity
				tempTempt = RemoteEntities::Desires::TempDesireTempt.empty
				tempTempt.setup self.remote_entity
				self.bp = tempSit
				self.bq = tempTempt
			end

			override_for :method => :random_sound
			def ocelote_random_sound
				if self.is_tamed
					return self.remote_entity.get_sound(RemoteEntities::EntitySound::RANDOM, 'purr') if self.cc # todo what's cc
					self.remote_entity.get_sound RemoteEntities::EntitySound::RANDOM, (self.random.next_int(4) == 0 ? 'purreow' : 'meow')
				else
					''
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