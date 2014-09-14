require '../base'
require '../namespace'
require 'base_attacking_entity'
require '../mixins/custom_overrider'
require '../mixins/sounds'
require '../mixins/entity_handle'
require '../mixins/remote_update'

module RemoteEntities
	module Entities
		class RemoteBatImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteBat
			include RemoteEntities::EntityMixins::EntitySounds

			def is_hanging
				self.entity.is_hanging?
			end

			def set_hanging(in_hanging)
				self.entity.hanging = in_hanging
			end
		end

		class RemoteBatEntity < NMS::EntityBat
			extend RemoteEntities::EntityMixins::EntityHandle
			extend RemoteEntities::EntityMixins::CustomOverrider
			extend RemoteEntities::EntityMixins::RemoteUpdate
		end
	end
end