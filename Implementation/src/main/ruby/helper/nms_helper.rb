require 'java'
require '../namespace'
require '../base'

module RemoteEntities
	module Helpers
		module NMSHelper
			def self.temp_insentient #TODO need to debug
				if @@temp
					@@temp
				else
					@@temp = Java::JavaClass.for_name("net.minecraft.server.#{$MINECRAFT_VERSION}.EntityInsentient").ruby_class.new nil
				end
			end

			def are_items_equal?(in_item1, in_item2)
				unless in_item1.do_materials_match(in_item2)
					return false
				end

				if in_item1.uses_data != in_item2.uses_data or in_item1.data != in_item2.data
					return false
				end

				if in_item1.tag.nil?
					in_item2.ni?
				else
					(not in_item2.nil?) and in_item1.tag == in_item2.tag
				end
			end
		end
	end
end