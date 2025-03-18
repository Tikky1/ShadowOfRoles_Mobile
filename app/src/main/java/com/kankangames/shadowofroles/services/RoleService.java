package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.analyst.DarkRevealer;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.analyst.Darkseer;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.killing.Psycho;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.Blinder;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.Disguiser;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.Interrupter;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.LastJoke;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.analyst.Detective;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.analyst.Observer;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.analyst.Stalker;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.protector.Soulbinder;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.support.SealMaster;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.chaos.Clown;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.chaos.ChillGuy;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.protector.FolkHero;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.unique.Entrepreneur;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.killing.Assassin;
import com.kankangames.shadowofroles.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.models.roles.enums.Team;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

import java.util.*;

public final class RoleService {
    private RoleService(){}
    private static final HashMap<Team, List<RoleTemplate>> rolesMap = new HashMap<>();
    private static final HashMap<RoleCategory, List<RoleTemplate>> categoryMap = new HashMap<>();
    private static final List<RoleTemplate> allRoles = new ArrayList<>();

    // Adds all roles to the catalog
    static {
        addRole(new Detective(), new Psycho(), new Observer(), new Soulbinder(), new Stalker(),
                new DarkRevealer(), new Interrupter(), new SealMaster(), new Assassin(),
                new ChillGuy(), new Clown(), new Disguiser(), new Darkseer(), new Blinder(),
                new LastJoke(), new FolkHero(), new Entrepreneur(), new Lorekeeper()
        );

    }


    /**
     * Adds role to the catalog
     * @param roles the roles to be added to the role catalog
     */
    private static void addRole(final RoleTemplate... roles){
        for(RoleTemplate role: roles){
            rolesMap.computeIfAbsent(role.getWinningTeam().getTeam(), k->new ArrayList<>()).add(role);
            categoryMap.computeIfAbsent(role.getRoleCategory(), k-> new ArrayList<>()).add(role);
            allRoles.add(role);
        }

    }

    /**
     *
     * @param team the desired team
     * @return a list that consist of the desired team
     */
    public static List<RoleTemplate> getRolesByTeam(final Team team){
        return rolesMap.getOrDefault(team, Collections.emptyList());
    }

    /**
     *
     * @param roleCategory the desired category
     * @return a list that consist of the desired category
     */
    public static List<RoleTemplate> getRolesByCategory(final RoleCategory roleCategory){
        return categoryMap.getOrDefault(roleCategory, Collections.emptyList());
    }

    /**
     *
     * @return a copy array list of all roles
     */
    public static List<RoleTemplate> getAllRoles(){
        return new ArrayList<>(allRoles);
    }

    /**
     *
     * @param otherRole the role that is not wanted to return
     * @return a random role other than the parameter role
     */
    public static RoleTemplate getRandomRole(final RoleTemplate otherRole){
        ArrayList<RoleTemplate> otherRoles = new ArrayList<>(allRoles);
        otherRoles.remove(otherRole);
        return otherRoles.get(new Random().nextInt(otherRoles.size())).copy();
    }

    /**
     *
     * @return a random role in the catalog
     */
    public static RoleTemplate getRandomRole(){
        return allRoles.get(new Random().nextInt(allRoles.size())).copy();
    }

    /**
     * Selects the role pool in the game according to player count
     * @param playerCount the count of the players
     * @return an array list that is the players' roles
     */
    public static ArrayList<RoleTemplate> initializeRoles(final int playerCount){
        HashMap<RoleTemplate, Integer> roles;

        switch (playerCount) {
            case 5:
                roles = configureFivePlayers();
                break;
            case 6:
                roles = configureSixPlayers();
                break;
            case 7:
                roles = configureSevenPlayers();
                break;
            case 8:
                roles = configureEightPlayers();
                break;
            case 9:
                roles = configureNinePlayers();
                break;
            case 10:
                roles = configureTenPlayers();
                break;
            default:
                throw new IllegalStateException("Unexpected player count: " + playerCount);
        }
        ArrayList<RoleTemplate> rolesList = new ArrayList<>();
        for(final Map.Entry<RoleTemplate,Integer> entry : roles.entrySet()){

            for(int i=0;i<entry.getValue();i++){
                rolesList.add(entry.getKey().copy());
            }
        }
        Collections.shuffle(rolesList);

        return rolesList;
    }

    /**
     * Adds the role to the roles hashmap
     * @param roles the hash map of the roles that is created currently
     * @param role the role to be added to the hashmap
     */
    private static void putRole(HashMap<RoleTemplate,Integer> roles, RoleTemplate role){
        roles.put(role, roles.getOrDefault(role,0)+1);
    }

    /**
     * Configures roles for a 5-player game.
     */
    private static HashMap<RoleTemplate,Integer> configureFivePlayers(){
        HashMap<RoleTemplate,Integer> roles = new HashMap<>();
        putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.FOLK_ANALYST));
        putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.FOLK_SUPPORT, RoleCategory.FOLK_PROTECTOR));
        putRole(roles, getRoleByTeamWithProbability(roles,Team.FOLK));
        putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_KILLING));
        putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));

        return roles;
    }

    /**
     * Configures roles for a 6-player game.
     */
    private static HashMap<RoleTemplate,Integer> configureSixPlayers(){
        HashMap<RoleTemplate,Integer> roles = configureFivePlayers();
        putRole(roles, getRoleByTeamWithProbability(roles,Team.FOLK));
        return roles;
    }

    /**
     * Configures roles for a 7-player game.
     */
    private static HashMap<RoleTemplate,Integer> configureSevenPlayers(){
        HashMap<RoleTemplate,Integer> roles = configureSixPlayers();
        switch (new Random().nextInt(2)){
            case 0: putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                break;
            case 1: putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_ANALYST,RoleCategory.CORRUPTER_SUPPORT));
                break;
        }

        return roles;
    }

    /**
     * Configures roles for an 8-player game.
     */
    private static HashMap<RoleTemplate,Integer> configureEightPlayers(){

        HashMap<RoleTemplate,Integer> roles = configureSixPlayers();

        switch (new Random().nextInt(3)){
            case 0:
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                break;
            case 1:
                putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_ANALYST,RoleCategory.CORRUPTER_SUPPORT));
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                break;
            case 2:
                putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_ANALYST,RoleCategory.CORRUPTER_SUPPORT));
                putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_ANALYST,RoleCategory.CORRUPTER_SUPPORT));
                break;
        }

        return roles;
    }

    /**
     * Configures roles for a 9-player game.
     */
    private static HashMap<RoleTemplate,Integer> configureNinePlayers(){
        HashMap<RoleTemplate,Integer> roles = configureEightPlayers();
        putRole(roles, getRoleByTeamWithProbability(roles,Team.FOLK));
        return roles;
    }

    /**
     * Configures roles for a 10-player game.
     */
    private static HashMap<RoleTemplate,Integer> configureTenPlayers(){

        HashMap<RoleTemplate,Integer> roles = configureSixPlayers();
        putRole(roles, getRoleByTeamWithProbability(roles,Team.FOLK));
        putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_SUPPORT,RoleCategory.CORRUPTER_ANALYST));

        switch (new Random().nextInt(4)){
            case 0:
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                break;
            case 1:
                putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_ANALYST,RoleCategory.CORRUPTER_SUPPORT));
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                break;
            case 2:
                putRole(roles, getRoleByCategoryWithProbability(roles,RoleCategory.CORRUPTER_ANALYST,RoleCategory.CORRUPTER_SUPPORT));
                putRole(roles, getRoleByTeamWithProbability(roles,Team.FOLK));
                break;
            case 3:
                putRole(roles, getRoleByTeamWithProbability(roles,Team.NEUTRAL));
                putRole(roles, getRoleByTeamWithProbability(roles,Team.FOLK));
                break;
        }

        return roles;
    }

    /**
     *
     * @param currentRoles the hash map of the roles that is created currently
     * @param roleCategory desired category
     * @return the role that is generated from the category list
     */
    private static RoleTemplate getRoleByCategoryWithProbability(HashMap<RoleTemplate,Integer> currentRoles, RoleCategory roleCategory){
        List<RoleTemplate> roles = new ArrayList<>(getRolesByCategory(roleCategory));
        removeMaxCount(currentRoles,roles);

        return getRoleWithProbability(roles);
    }

    /**
     *
     * @param currentRoles the hash map of the roles that is created currently
     * @param roleCategories desired categories
     * @return the role that is generated from the categories list
     */
    private static RoleTemplate getRoleByCategoryWithProbability(HashMap<RoleTemplate,Integer> currentRoles , RoleCategory... roleCategories){
        List<RoleTemplate> roles = new ArrayList<>();
        for(RoleCategory roleCategory: roleCategories){

            roles.addAll(getRolesByCategory(roleCategory));
        }
        removeMaxCount(currentRoles,roles);
        return getRoleWithProbability(roles);
    }

    /**
     *
     * @param currentRoles the hash map of the roles that is created currently
     * @param team desired team
     * @return the role that is generated from the team list
     */
    private static RoleTemplate getRoleByTeamWithProbability(HashMap<RoleTemplate,Integer> currentRoles, Team team){

        List<RoleTemplate> roles = new ArrayList<>(getRolesByTeam(team));
        removeMaxCount(currentRoles,roles);
        return getRoleWithProbability(roles);
    }

    /**
     * Removes the roles that are already in their max count
     * @param currentRoles the hash map of the roles that is created currently
     * @param randomRoleList the list that consists of desired roles
     */
    private static void removeMaxCount(HashMap<RoleTemplate,Integer> currentRoles, List<RoleTemplate> randomRoleList){
        for(Map.Entry<RoleTemplate,Integer> entry : currentRoles.entrySet()){
            if(entry.getKey().getChanceProperty().getMaxNumber()<=entry.getValue()){
                randomRoleList.remove(entry.getKey());
            }
        }
    }

    /**
     * @param randomRoleList the list that consists of desired roles
     * @return a generated role from the randomRoleList with the probability of the roles
     */
    private static RoleTemplate getRoleWithProbability(List<RoleTemplate> randomRoleList){

        int sum = randomRoleList.stream().mapToInt(role -> role.getChanceProperty().getChance()).sum();
        int randNum = new Random().nextInt(sum);
        int currentSum = 0;

        for (RoleTemplate role : randomRoleList) {
            currentSum += role.getChanceProperty().getChance();

            if (currentSum >= randNum) {
                return role.copy();
            }
        }
        return null;
    }


}
