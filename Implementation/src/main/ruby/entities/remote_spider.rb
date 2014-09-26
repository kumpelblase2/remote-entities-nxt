require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSpiderImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteSpider
		end

		class RemoteSpiderEntity < NMS::EntitySpider
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