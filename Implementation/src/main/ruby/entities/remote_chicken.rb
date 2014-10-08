require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteChickenImpl < BaseEntity
			include RemoteEntities::Entities::RemoteChicken
		end

		class RemoteChickenEntity < NMS::EntityChicken
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

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