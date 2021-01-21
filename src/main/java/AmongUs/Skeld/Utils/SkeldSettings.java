package AmongUs.Skeld.Utils;

public class SkeldSettings {

    private static int impostor;
    private static boolean confirmEjects;
    private static int emergencyMeetings;
    private static boolean anonymousVoting;
    private static int emergencyCooldown;
    private static int discussionTime;
    private static int votingTime;
    private static double playerSpeed;
    private static double killCooldown;
    private static int killDistance;
    private static boolean visualTasks;
    private static int taskBarUpdate;
    private static int commonTasks;
    private static int longTasks;
    private static int shortTasks;

    public SkeldSettings() {
        this.recommendedSettings();
    }

    public void recommendedSettings() {
        impostor = 2;
        confirmEjects = true;
        emergencyMeetings = 1;
        anonymousVoting = false;
        emergencyCooldown = 15;
        discussionTime = 15;
        votingTime = 120;
        playerSpeed = 1.0;
        killCooldown = 45.0;
        killDistance = 2;
        visualTasks = true;
        taskBarUpdate = 1;
        commonTasks = 1;
        longTasks = 1;
        shortTasks = 2;
    }

    public int getImpostor() {
        return impostor;
    }

    public void setImpostor(int impostor) {
        this.impostor = impostor;
    }

    public boolean isConfirmEjects() {
        return confirmEjects;
    }

    public void setConfirmEjects(boolean confirmEjects) {
        this.confirmEjects = confirmEjects;
    }

    public int getEmergencyMeetings() {
        return emergencyMeetings;
    }

    public void setEmergencyMeetings(int emergencyMeetings) {
        this.emergencyMeetings = emergencyMeetings;
    }

    public boolean isAnonymousVoting() {
        return anonymousVoting;
    }

    public void setAnonymousVoting(boolean anonymousVoting) {
        this.anonymousVoting = anonymousVoting;
    }

    public int getEmergencyCooldown() {
        return emergencyCooldown;
    }

    public void setEmergencyCooldown(int emergencyCooldown) {
        this.emergencyCooldown = emergencyCooldown;
    }

    public int getDiscussionTime() {
        return discussionTime;
    }

    public void setDiscussionTime(int discussionTime) {
        this.discussionTime = discussionTime;
    }

    public int getVotingTime() {
        return votingTime;
    }

    public void setVotingTime(int votingTime) {
        this.votingTime = votingTime;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public double getKillCooldown() {
        return killCooldown;
    }

    public void setKillCooldown(double killCooldown) {
        this.killCooldown = killCooldown;
    }

    public int getKillDistance() {
        return killDistance;
    }

    public void setKillDistance(int killDistance) {
        this.killDistance = killDistance;
    }

    public boolean isVisualTasks() {
        return visualTasks;
    }

    public void setVisualTasks(boolean visualTasks) {
        this.visualTasks = visualTasks;
    }

    public int getTaskBarUpdate() {
        return taskBarUpdate;
    }

    public void setTaskBarUpdate(int taskBarUpdate) {
        this.taskBarUpdate = taskBarUpdate;
    }

    public int getCommonTasks() {
        return commonTasks;
    }

    public void setCommonTasks(int commonTasks) {
        this.commonTasks = commonTasks;
    }

    public int getLongTasks() {
        return longTasks;
    }

    public void setLongTasks(int longTasks) {
        this.longTasks = longTasks;
    }

    public int getShortTasks() {
        return shortTasks;
    }

    public void setShortTasks(int shortTasks) {
        this.shortTasks = shortTasks;
    }
}
