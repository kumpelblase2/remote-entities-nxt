require '../base'

module RemoteEntities
	module Desires
		class DesireSwim < RemoteEntities::Thinking::DesireBase
			def initialize
				super
				self.type = RemoteEntities::Thinking::DesireType::MOVEMENT_ADDITION
			end

			def on_add(in_entity)
				super
				self.remote_entity.entity.navigation.can_swim = true
			end

			def should_execute
				self.remote_entity.entity.M || self.remote_entity.entity.P
			end

			def update
				self.remote_entity.entity.controller_jump.a if self.remote_entity.entity.random.next_float < 0.8
				true
			end
		end
	end
end