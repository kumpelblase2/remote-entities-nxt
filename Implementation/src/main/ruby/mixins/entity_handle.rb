require '../base'
require '../namespace'
require '../policy_pwn'

module RemoteEntities
	module EntityMixins
		module EntityHandle
			class << self
				attr_reader :remote_entity
				attr_reader :goalSelector
				attr_reader :targetSelector
			end

			def setup_for(in_entity)
				@remote_entity = in_entity
				RemoteEntities::Helpers::World::PathfinderGoalSelectorHelper.new(self.goalSelector).clear_goals
				RemoteEntities::Helpers::World::PathfinderGoalSelectorHelper.new(self.targetSelector).clear_goals
			end
		end
	end
end