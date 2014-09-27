require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSheepImpl < BaseEntity
			include RemoteEntities::Entities::RemoteSheep
		end

		class RemoteSheepEntity < NMS::EntitySheep
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