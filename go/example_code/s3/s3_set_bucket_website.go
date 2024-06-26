// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package main

import (
	"fmt"
	"os"
	"path/filepath"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/s3"
)

// Sets the bucket's website configuration. Allows setting the index suffix,
// and an optional error page keys.
//
// If the bucket already has a website configured on it this will overwrite
// that configuration
//
// Usage:
//
//	go run s3_set_bucket_website.go BUCKET_NAME INDEX_PAGE ERROR_PAGE
func main() {
	if len(os.Args) != 4 {
		exitErrorf("bucket name and index suffix page required\nUsage: %s bucket_name index_page [error_page]",
			filepath.Base(os.Args[0]))
	}

	bucket := fromArgs(os.Args, 1)
	indexSuffix := fromArgs(os.Args, 2)
	errorPage := fromArgs(os.Args, 3)

	// Initialize a session in us-west-2 that the SDK will use to load
	// credentials from the shared credentials file ~/.aws/credentials.
	sess, err := session.NewSession(&aws.Config{
		Region: aws.String("us-west-2")},
	)

	// Create S3 service client
	svc := s3.New(sess)

	// Create SetBucketWebsite parameters based on CLI input
	params := s3.PutBucketWebsiteInput{
		Bucket: aws.String(bucket),
		WebsiteConfiguration: &s3.WebsiteConfiguration{
			IndexDocument: &s3.IndexDocument{
				Suffix: aws.String(indexSuffix),
			},
		},
	}

	// Add the error page if set on CLI
	if len(errorPage) > 0 {
		params.WebsiteConfiguration.ErrorDocument = &s3.ErrorDocument{
			Key: aws.String(errorPage),
		}
	}

	// Set the website configuration on the bucket. Replacing any existing
	// configuration.
	_, err = svc.PutBucketWebsite(&params)
	if err != nil {
		exitErrorf("Unable to set bucket %q website configuration, %v",
			bucket, err)
	}

	fmt.Printf("Successfully set bucket %q website configuration\n", bucket)
}

func exitErrorf(msg string, args ...interface{}) {
	fmt.Fprintf(os.Stderr, msg+"\n", args...)
	os.Exit(1)
}

func fromArgs(args []string, idx int) string {
	if len(args) > idx {
		return args[idx]
	}
	return ""
}
