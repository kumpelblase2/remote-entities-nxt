require 'namespace'
require 'base'

module RemoteEntities
	module Features
		module FeatureRegistration
			class << self
				Java::de.kumpelblase2.remoteentities.api.features.Features.register_feature self.java_class
			end
		end
	end
end