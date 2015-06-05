package hu.bankmonitor.starter.microservice.common.log;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.awslogs")
@Getter
@Setter
public class AwsLogsProperties {

	private String awsAccessKey;

	private String awsSecretKey;

	private String awsRegionName;

	private String logGroupName;

	private String logStreamName;

}
