require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteCaveSpiderImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteCaveSpider
		end

		class RemoteCaveSpiderEntity < NMS::EntityCaveSpider
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