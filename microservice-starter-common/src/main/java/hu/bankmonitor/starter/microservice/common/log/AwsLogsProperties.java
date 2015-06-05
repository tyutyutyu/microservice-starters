package hu.bankmonitor.starter.microservice.common.log;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = { "application.awslogs.awsAccessKey", "application.awslogs.awsSecretKey" })
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
