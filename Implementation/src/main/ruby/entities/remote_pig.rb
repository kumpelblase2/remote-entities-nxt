require '../base'
require '../namespace'

module RemoteEntities
	module Desires
		class TempFollowCarrotStick < NMS::PathfinderGoalPassengerCarrotStick
			def setup(in_remote)
				@remote = in_remote
			end

			def f
				return true unless remote_desire
				remote_desire.is_speed_boosted
			end

			def g
				remote_desire.boost_speed if remote_desire
			end

			def h
				return false unless remote_desire
				remote_desire.is_controlled_by_player
			end

			def remote_desire
				@remote.mind.get_movement_desire(RemoteEntities::Desires::DesireFollowCarrotStick)
			end
		end
	end

	module Entities
		class RemotePigImpl < BaseEntity
			include RemoteEntities::Entities::RemotePig
		end

		class RemotePigEntity < NMS::EntityPig
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			def on_create
				temp = RemoteEntities::Desires::TempFollowCarrotStick.new nil, 0
				temp.setup self.remote_entity
				self.bp = temp
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