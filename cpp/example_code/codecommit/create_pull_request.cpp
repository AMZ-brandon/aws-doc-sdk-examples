// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

#include <aws/core/Aws.h>
#include <aws/codecommit/CodeCommitClient.h>
#include <aws/codecommit/model/CreatePullRequestRequest.h>
#include <aws/codecommit/model/CreatePullRequestResult.h>
#include <aws/codecommit/model/Target.h>
#include <aws/core/utils/Outcome.h>
#include <iostream>

/**
 * Creates a pull request based with title, description, repository name, source branch
 * and destination branch name based on command line inputs
 */

int main(int argc, char ** argv)
{
  if (argc != 6)
  {
    std::cout << "Usage: create_pull_request <title> <description> <repository_name>"
              << "<source_branch_name> <destination_branch_name>" << std::endl;
    return 1;
  }

  Aws::SDKOptions options;
  Aws::InitAPI(options);
  {
    Aws::String title(argv[1]);
    Aws::String description(argv[2]);
    Aws::String repository_name(argv[3]);
    Aws::String source_branch_name(argv[4]);
    Aws::String destination_branch_name(argv[5]);

    Aws::CodeCommit::CodeCommitClient codecommit;

    Aws::CodeCommit::Model::Target targets;
    Aws::CodeCommit::Model::CreatePullRequestRequest cpr_req;

    targets.SetRepositoryName(repository_name);
    targets.SetSourceReference(source_branch_name);
    targets.SetDestinationReference(destination_branch_name);

    cpr_req.SetTitle(title);
    cpr_req.SetDescription(description);
    cpr_req.AddTargets(targets);

    auto cpr_out = codecommit.CreatePullRequest(cpr_req);

    if (cpr_out.IsSuccess())
    {
      std::cout << "Successfully created pull request " << std::endl;
    }
    else
    {
      std::cout << "Error creating pull request " << cpr_out.GetError().GetMessage()
        << std::endl;
    }
  }

  Aws::ShutdownAPI(options);
  return 0;
}
