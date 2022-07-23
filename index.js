const axios = require('axios').default;

async function getUser() {
    try {
      
      const response = await axios.get('https://play.clickhouse.com', {
        params: {
          "user": "explorer"
        },
        data: `
                SELECT
                    repo_name,
                    count() AS stars
                FROM github_events
                WHERE (event_type = 'WatchEvent') AND (actor_login IN
                (
                    SELECT actor_login
                    FROM github_events
                    WHERE (event_type = 'WatchEvent') AND (repo_name IN ('apache/pulsar'))
                )) AND (repo_name NOT IN ('apache/pulsar'))
                GROUP BY repo_name
                ORDER BY stars DESC
                LIMIT 50
                FORMAT JSON
        `
      });
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
}

getUser()
