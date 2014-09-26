require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSquidImpl < BaseEntity
			include RemoteEntities::Entities::RemoteSquid
		end

		class RemoteSquidEntity < NMS::EntitySquid
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