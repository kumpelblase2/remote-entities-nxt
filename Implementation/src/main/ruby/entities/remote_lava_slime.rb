require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteLavaSlimeImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteLavaSlime
		end

		class RemoteLavaSlimeEntity < NMS::EntityMagmaCube
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults
			include RemoteEntities::EntityMixins::RemoteSlimeUpdate

			def on_create
				@jump_delay = self.random.next_int(20) + 10
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