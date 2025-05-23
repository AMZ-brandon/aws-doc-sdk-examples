# Amazon EC2 code examples for the SDK for .NET

## Overview

Shows how to use the AWS SDK for .NET to work with Amazon Elastic Compute Cloud (Amazon EC2).

<!--custom.overview.start-->
<!--custom.overview.end-->

_Amazon EC2 is a web service that provides resizable computing capacity—literally, servers in Amazon's data centers—that you use to build and host your software systems._

## ⚠ Important

* Running this code might result in charges to your AWS account. For more details, see [AWS Pricing](https://aws.amazon.com/pricing/) and [Free Tier](https://aws.amazon.com/free/).
* Running the tests might result in charges to your AWS account.
* We recommend that you grant your code least privilege. At most, grant only the minimum permissions required to perform the task. For more information, see [Grant least privilege](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html#grant-least-privilege).
* This code is not tested in every AWS Region. For more information, see [AWS Regional Services](https://aws.amazon.com/about-aws/global-infrastructure/regional-product-services).

<!--custom.important.start-->
<!--custom.important.end-->

## Code examples

### Prerequisites

For prerequisites, see the [README](../README.md#Prerequisites) in the `dotnetv3` folder.


<!--custom.prerequisites.start-->
<!--custom.prerequisites.end-->

### Get started

- [Hello Amazon EC2](Actions/HelloEC2.cs#L4) (`DescribeSecurityGroups`)


### Basics

Code examples that show you how to perform the essential operations within a service.

- [Learn the basics](Scenarios/EC2_Basics/EC2Basics.cs)


### Single actions

Code excerpts that show you how to call individual service functions.

- [AllocateAddress](Actions/EC2Wrapper.cs#L28)
- [AssociateAddress](Actions/EC2Wrapper.cs#L64)
- [AuthorizeSecurityGroupIngress](Actions/EC2Wrapper.cs#L107)
- [CreateKeyPair](Actions/EC2Wrapper.cs#L170)
- [CreateSecurityGroup](Actions/EC2Wrapper.cs#L242)
- [DeleteKeyPair](Actions/EC2Wrapper.cs#L319)
- [DeleteSecurityGroup](Actions/EC2Wrapper.cs#L361)
- [DescribeInstanceTypes](Actions/EC2Wrapper.cs#L531)
- [DescribeInstances](Actions/EC2Wrapper.cs#L474)
- [DescribeKeyPairs](Actions/EC2Wrapper.cs#L578)
- [DescribeSecurityGroups](Actions/EC2Wrapper.cs#L620)
- [DisassociateAddress](Actions/EC2Wrapper.cs#L714)
- [ReleaseAddress](Actions/EC2Wrapper.cs#L802)
- [RunInstances](Actions/EC2Wrapper.cs#L837)
- [StartInstances](Actions/EC2Wrapper.cs#L890)
- [StopInstances](Actions/EC2Wrapper.cs#L930)
- [TerminateInstances](Actions/EC2Wrapper.cs#L971)

<!--custom.examples.start-->
<!--custom.examples.end-->

## Run the examples

### Instructions

For general instructions to run the examples, see the
[README](../README.md#building-and-running-the-code-examples) in the `dotnetv4` folder.

Some projects might include a settings.json file. Before compiling the project,
you can change these values to match your own account and resources. Alternatively,
add a settings.local.json file with your local settings, which will be loaded automatically
when the application runs.

After the example compiles, you can run it from the command line. To do so, navigate to
the folder that contains the .csproj file and run the following command:

```
dotnet run
```

Alternatively, you can run the example from within your IDE.


<!--custom.instructions.start-->
<!--custom.instructions.end-->

#### Hello Amazon EC2

This example shows you how to get started using Amazon EC2.


#### Learn the basics

This example shows you how to do the following:

- Create a key pair and security group.
- Select an Amazon Machine Image (AMI) and compatible instance type, then create an instance.
- Stop and restart the instance.
- Associate an Elastic IP address with your instance.
- Connect to your instance with SSH, then clean up resources.

<!--custom.basic_prereqs.ec2_Scenario_GetStartedInstances.start-->
<!--custom.basic_prereqs.ec2_Scenario_GetStartedInstances.end-->


<!--custom.basics.ec2_Scenario_GetStartedInstances.start-->
<!--custom.basics.ec2_Scenario_GetStartedInstances.end-->

### Tests

⚠ Running tests might result in charges to your AWS account.


To find instructions for running these tests, see the [README](../README.md#Tests)
in the `dotnetv4` folder.



<!--custom.tests.start-->
<!--custom.tests.end-->

## Additional resources

- [Amazon EC2 User Guide](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/concepts.html)
- [Amazon EC2 API Reference](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/Welcome.html)
- [SDK for .NET Amazon EC2 reference](https://docs.aws.amazon.com/sdkfornet/v3/apidocs/items/EC2/NEC2.html)

<!--custom.resources.start-->
<!--custom.resources.end-->

---

Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0