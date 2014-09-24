require 'java'
require 'namespace'
require 'extensions'

module RemoteEntities
	include_package 'de.kumpelblase2.remoteentities.api'

	def self.plugin
		$PLUGIN
	end

	module Entities
		include_package 'de.kumpelblase2.remoteentities.entities'
	end

	module Exceptions
		include_package 'de.kumpelblase2.remoteentities.exceptions'
	end

	module Thinking
		include_package 'de.kumpelblase2.remoteentities.api.thinking'
	end

	module Features
		include_package 'de.kumpelblase2.remoteentities.api.features'
	end

	module Helpers
		include_package 'de.kumpelblase2.remoteentities.helper'

		module Ruby
			include_package 'de.kumpelblase2.remoteentities.helper.ruby'
		end

		module World
			include_package 'de.kumpelblase2.remoteentities.helper.world'
		end
	end

	module Events
		include_package 'de.kumpelblase2.remoteentities.api.events'
	end

	REMOTE_DEFINITION_BASE = plugin.config.get_string 'update.url'
	REMOTE_DEFINITION_VERSIONS = "#{REMOTE_DEFINITION_BASE}/versions"
	REMOTE_DEFINITION_LIST_URL = "#{REMOTE_DEFINITION_VERSIONS}/list"
end

NMS_PACKAGE = "net.minecraft.server.#{$MC_VERSION}"
CB_PACKAGE = "org.bukkit.craftbukkit.#{$MC_VERSION}"

module NMS
	include_package NMS_PACKAGE
end

module CB # Can you automatically map this? -> Theoretically yes, but not reliably.
	include_package CB_PACKAGE

	module Inventory
		include_package "#{CB_PACKAGE}.inventory"
	end
end

module Bukkit
	include_package 'org.bukkit'
end

def bukkit
	Java::org.bukkit.Bukkit
end