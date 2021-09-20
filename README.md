# KED - Stats (per speaker)

This repository contains code that computes various stats about KED (Knowledge
Exchange Day, an internal conference day at Publicis Sapient France) talks.

Currently, it only computes stats for ratings of talks, making it query-able by
speaker.

## Example calls

### HTTPie

```http POST https://exys1bnar5.execute-api.eu-west-1.amazonaws.com/Prod/stats/speaker/ Content-Type:application/json speaker=achotard year=2021 month=5```

```
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 302
Content-Type: application/json
Date: Mon, 20 Sep 2021 22:09:54 GMT
Via: 1.1 7d935e83126b0b85ded112b940f9c85d.cloudfront.net (CloudFront)
X-Amz-Cf-Id: 1gOKdzndX_FktlG5V_8wDWSHMZGUIlChDYJUlN5056ZHDYFoyyivkw==
X-Amz-Cf-Pop: CDG52-P1
X-Amzn-Trace-Id: Root=1-614906b2-4add02184f28fec6365b5760;Sampled=0
X-Cache: Miss from cloudfront
X-Custom-Header: application/json
x-amz-apigw-id: F-374FfqjoEF2sQ=
x-amzn-RequestId: 00802401-de39-4428-a5bd-9507d949d39c
```

```json
[
    {
        "averageRating": 4.375,
        "numberOfRatings": 8,
        "title": "Lightning talks : du git, du gitlab, du mot de passe et OVH"
    },
    {
        "averageRating": 4.533333333333333,
        "numberOfRatings": 15,
        "title": "GitOps - Fuyez, pauvres fous ?"
    },
    {
        "averageRating": 4.666666666666667,
        "numberOfRatings": 3,
        "title": "Fondation Cloud Native"
    }
]
```
