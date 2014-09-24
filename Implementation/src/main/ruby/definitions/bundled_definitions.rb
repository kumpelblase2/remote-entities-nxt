require '../namespace'

module RemoteEntities
	METHODS = {
		:mc_version => 'v1_7_R1',
		:version => '1.0',
		:definitions => {
			:entities => {
				:default => {
					:random => 'aI',
					:tick => 'h',
					:new_ai? => 'bk',
					:push => {
						:name => 'g',
						:params => %w(double double double)
					},
					:apply_motion => {
						:name => 'e',
						:params => %w(float float)
					},
					:random_sound => 't',
					:hurt_sound => 'aT',
					:death_sound => 'aU',
					:do_step_sound_at => {
						:name => 'a',
						:params => %w(int int nms.Block) #TODO nms.Block might not be good enough
					},
					:distance_to_entity => {
						:name => 'e',
						:params => %w(nms.Entity)
					},
					:distance_to_location => {
						:name => 'e',
						:params => %w(double double double)
					}
				},
				:creature => {
					:has_home_area? => 'bS',
					:home_range => 'bU',
					:relative_light_power_at => {
							:name => 'a',
							:params => %w(int int int)
					}
				},
				:insentient => {
					:on_leash? => 'bH',
					:can_be_steered? => 'bC',
					:max_head_rotation => 'x',
					:p => 'p' # TODO
				},
				:living => {
					:last_attack_tick => 'aK'
				},
				:bat => {
					:tick_movement => 'bq',
					:is_hanging? => 'bN',
					:hanging= => {
						:name => 'a',
						:params => %w(boolean)
					}
				},
				:wolf => {
					:sitting= => {
						:name => 'm',
						:params => %w(boolean)
					}
				},
				:animal => {
					:is_affected? => 'cc',
					:reset_affection => 'cd'
				},
				:ironGolem => {
					:flower_in_hand= => {
						:name => 'a',
						:params => %w(boolean)
					}
				}
			},
			:nms => {
				:World => {
					:is_day? => 'v',
					:get_typed_entities_in => {
						:name => 'a',
						:params => %w(Class nms.BoundingBox)
					},
					:get_typed_entities_in_with_selector => {
						:name => 'a',
						:params => %w(Class nms.BoundingBox nms.IEntitySelector)
					},
					:has_sunlight_at? => {
						:name => 'i',
						:params => %w(int int int)
					},
					:break_status= => {
						:name => 'd',
						:params => %w(int int int int int)
					},
					:light_power_at => {
						:name => 'n',
						:params => %w(int int int)
					}
				},
				:Navigation => {
					:stop => 'h',
					:has_path? => 'g',
					:update => 'f',
					:speed= => {
						:name => 'a',
						:params => %w(int)
					},
					:create_path => {
						:name => 'a',
						:params => %w(double double double)
					},
					:can_swim= => {
						:name => 'e',
						:params => %w(boolean)
					}
				},
				:BlockDoor => {
					:f => { #TODO
						:name => 'f',
						:params => %w(nms.IBlockAccess int int int)
					}
				},
				:EnumDifficulty => {
					:int_value => 'a'
				},
				:Block => {
					:id_by_type => {
						:name => 'b',
						:params => %w(nms.Block),
						:scope => 'class'
					}
				}
			},
			:cb => {
				:entity => {
					:CraftEntity => {
						:nms_handle => 'get_handle'
					}
				},
				:CraftWorld => {
					:nms_handle => 'get_handle'
				}
			},
			:custom => {
				'de.kumpelblase2.whatever'.to_sym => {
					:TheClass => {
						:update => {
							:name => 'boop',
							:params => %w(beep beep)
						}
					}
				}
			}
		}
	}
end