require 'base'
require 'policy_pwn'
require 'entity_setup'

def bootstrap
	RemoteEntities::PolicyPWN.get_rekt
	setup_entities
end

def setup_entities
	require 'config/entity_settings'
	$entity_config.each do |entity, data|
		RemoteEntities::Entities.setup entity do
			with_sounds if data[:sounds]
			with_classes *data[:impl]
		end
	end
end