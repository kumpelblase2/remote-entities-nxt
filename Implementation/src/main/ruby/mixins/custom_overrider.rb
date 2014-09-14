require '../base'
require '../namespace'

module RemoteEntities
	module EntityMixins
		module CustomOverrider
			@override_for = nil

			def override_for(opts = {})
				default = { :package => :nms }
				opts = default.merge opts
				@override_for = opts
			end

			def method_added(in_method)
				if @override_for
					package_defs = RemoteEntities::PolicyPWN.current_definitions[@override_for[:package]]
					class_defs = package_defs[@override_for[:type]]
					method_name = class_defs[@override_for[:method]]
					alias in_method method_name
					@override_for = nil
				end
			end
		end
	end
end