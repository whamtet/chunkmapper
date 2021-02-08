(ns chunkmapper.level-dat
  (:require
    [chunkmapper.nbt :as nbt]))

(defn level-dat []
  (nbt/->nbt
    `{:Data
      {:BorderWarningBlocks [:double 5.0],
       :BorderCenterZ [:double 0.0],
       :BorderDamagePerBlock [:double 0.2],
       :SpawnX [:int 64],
       :WanderingTraderSpawnDelay [:int 24000],
       :initialized [:byte 1],
       :BorderSizeLerpTarget [:double 6.0E7],
       :BorderWarningTime [:double 15.0],
       :WorldGenSettings
       {:bonus_chest [:byte 0],
        :seed [:long 699818219905139133],
        :generate_features [:byte 1],
        :dimensions
        {:minecraft:overworld
         {:generator
          {:settings "minecraft:overworld",
           :seed [:long 699818219905139133],
           :biome_source
           {:seed [:long 699818219905139133],
            :large_biomes [:byte 0],
            :type "minecraft:vanilla_layered"},
           :type "minecraft:noise"},
          :type "minecraft:overworld"},
         :minecraft:the_nether
         {:generator
          {:settings "minecraft:nether",
           :seed [:long 699818219905139133],
           :biome_source
           {:seed [:long 699818219905139133],
            :preset "minecraft:nether",
            :type "minecraft:multi_noise"},
           :type "minecraft:noise"},
          :type "minecraft:the_nether"},
         :minecraft:the_end
         {:generator
          {:settings "minecraft:end",
           :seed [:long 699818219905139133],
           :biome_source
           {:seed [:long 699818219905139133], :type "minecraft:the_end"},
           :type "minecraft:noise"},
          :type "minecraft:the_end"}}},
       :clearWeatherTime [:int 0],
       :WanderingTraderSpawnChance [:int 25],
       :LastPlayed [:long 1612553808898],
       :SpawnY [:int 70],
       :DataVersion [:int 2586],
       :SpawnZ [:int 80],
       :BorderSafeZone [:double 5.0],
       :GameRules
       {:commandBlockOutput "true",
        :doMobLoot "true",
        :sendCommandFeedback "true",
        :spectatorsGenerateChunks "true",
        :fallDamage "true",
        :maxCommandChainLength "65536",
        :fireDamage "true",
        :drowningDamage "true",
        :doImmediateRespawn "false",
        :reducedDebugInfo "false",
        :maxEntityCramming "24",
        :doFireTick "true",
        :doEntityDrops "true",
        :spawnRadius "10",
        :keepInventory "false",
        :universalAnger "false",
        :forgiveDeadPlayers "true",
        :doDaylightCycle "true",
        :mobGriefing "true",
        :randomTickSpeed "3",
        :doMobSpawning "true",
        :doTraderSpawning "true",
        :disableElytraMovementCheck "false",
        :showDeathMessages "true",
        :doInsomnia "true",
        :doTileDrops "true",
        :naturalRegeneration "true",
        :logAdminCommands "true",
        :doPatrolSpawning "true",
        :doWeatherCycle "true",
        :disableRaids "false",
        :doLimitedCrafting "false",
        :announceAdvancements "true"},
       :BorderSizeLerpTime [:long 0],
       :LevelName "New World",
       :thundering [:byte 0],
       :allowCommands [:byte 0],
       :ServerBrands ("vanilla"),
       :BorderSize [:double 6.0E7],
       :GameType [:int 0],
       :CustomBossEvents {},
       :rainTime [:int 64286],
       :DifficultyLocked [:byte 0],
       :thunderTime [:int 92335],
       :SpawnAngle [:float 0.0],
       :DataPacks {:Enabled ("vanilla"), :Disabled ()},
       :Time [:long 157],
       :BorderCenterX [:double 0.0],
       :Player
       {:foodSaturationLevel [:float 5.0],
        :SelectedItemSlot [:int 0],
        :foodLevel [:int 20],
        :Dimension "minecraft:overworld",
        :HurtTime [:short 0],
        :PortalCooldown [:int 0],
        :Score [:int 0],
        :Rotation ([:float 0.0] [:float 0.0]),
        :foodTickTimer [:int 0],
        :XpP [:float 0.0],
        :Motion ([:double 0.0] [:double -0.0784000015258789] [:double 0.0]),
        :DataVersion [:int 2586],
        :Pos ([:double 74.5] [:double 70.0] [:double 76.5]),
        :abilities
        {:invulnerable [:byte 0],
         :mayfly [:byte 0],
         :instabuild [:byte 0],
         :walkSpeed [:float 0.1],
         :mayBuild [:byte 1],
         :flying [:byte 0],
         :flySpeed [:float 0.05]},
        :foodExhaustionLevel [:float 0.0],
        :Fire [:short -20],
        :FallFlying [:byte 0],
        :seenCredits [:byte 0],
        :playerGameType [:int 0],
        :Invulnerable [:byte 0],
        :EnderItems (),
        :FallDistance [:float 0.0],
        :DeathTime [:short 0],
        :XpTotal [:int 0],
        :XpSeed [:int 0],
        :HurtByTimestamp [:int 0],
        :AbsorptionAmount [:float 0.0],
        :OnGround [:byte 1],
        :Air [:short 300],
        :Inventory (),
        :recipeBook
        {:isBlastingFurnaceGuiOpen [:byte 0],
         :isSmokerGuiOpen [:byte 0],
         :toBeDisplayed (),
         :isBlastingFurnaceFilteringCraftable [:byte 0],
         :recipes (),
         :isFurnaceGuiOpen [:byte 0],
         :isFilteringCraftable [:byte 0],
         :isSmokerFilteringCraftable [:byte 0],
         :isFurnaceFilteringCraftable [:byte 0],
         :isGuiOpen [:byte 0]},
        :UUID [:int-array (-1177994134 -965261488 -2069588330 -1020369339)],
        :SleepTimer [:short 0],
        :Attributes
        ({:Base [:double 0.10000000149011612],
          :Name "minecraft:generic.movement_speed"}),
        :Brain {:memories {}},
        :previousPlayerGameType [:int -1],
        :Health [:float 20.0],
        :XpLevel [:int 0]},
       :version [:int 19133],
       :DayTime [:long 157],
       :Version {:Snapshot [:byte 0], :Id [:int 2586], :Name "1.16.5"},
       :WasModded [:byte 0],
       :Difficulty [:byte 2],
       :hardcore [:byte 0],
       :raining [:byte 0],
       :DragonFight
       {:Gateways
        ([:int 9]
         [:int 13]
         [:int 3]
         [:int 11]
         [:int 16]
         [:int 7]
         [:int 6]
         [:int 12]
         [:int 14]
         [:int 18]
         [:int 0]
         [:int 10]
         [:int 8]
         [:int 15]
         [:int 4]
         [:int 5]
         [:int 1]
         [:int 19]
         [:int 2]
         [:int 17]),
        :DragonKilled [:byte 1],
        :PreviouslyKilled [:byte 1]},
       :ScheduledEvents ()}}))
