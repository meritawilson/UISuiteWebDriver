package webdrivermgr;

public class POJO {
	private String buildNumber;
	private String status;
	private String scenario;
	private String environment;
	private String userAction;
	private double timeTaken;
	private float totalSize;
	private int noOfRequests;
	private int RunId;

	public int getRunId() {
		return RunId;
	}

	public void setRunId(int runId) {
		RunId = runId;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public int getNoOfRequests() {
		return noOfRequests;
	}

	public void setNoOfRequests(int noOfRequests) {
		this.noOfRequests = noOfRequests;
	}

	public float getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(float totalSize) {
		this.totalSize = totalSize;
	}

	public double getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(double timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getUserAction() {
		return userAction;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
}
