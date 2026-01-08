# Deploy QuickSwap Backend to Render using Docker

This guide explains how to deploy the Spring Boot app to Render using the provided `Dockerfile`.

1. Push your repo to GitHub (already done).

2. On Render: New → Web Service → Connect your GitHub repo → select branch `main`.

3. For the Service Type choose: **Docker** (Render will use the `Dockerfile` in repo root).

4. Environment variables to add in Render (example keys):

- `SPRING_DATASOURCE_URL` = JDBC URL for your Postgres (e.g. `jdbc:postgresql://host:5432/dbname`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`
- `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD` (if using mail)
- `FIREBASE_SERVICE_ACCOUNT` (optional) — set the full Firebase JSON content as the value. The container start command writes this to `/app/serviceAccountKey.json` and sets `GOOGLE_APPLICATION_CREDENTIALS` automatically.

5. Build & Start: Render will build the Docker image using the `Dockerfile`. No custom build command necessary.

6. Local test commands (optional):

```bash
docker build -t quickswap-backend .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="<JDBC_URL>" \
  -e SPRING_DATASOURCE_USERNAME="<DB_USER>" \
  -e SPRING_DATASOURCE_PASSWORD="<DB_PASS>" \
  -e JWT_SECRET="<secret>" \
  -e CLOUDINARY_API_KEY="<key>" \
  -e CLOUDINARY_API_SECRET="<secret>" \
  -e FIREBASE_SERVICE_ACCOUNT="$(cat serviceAccountKey.json)" \
  quickswap-backend
```

7. Notes & troubleshooting

- Ensure your Postgres allows connections from Render or use Render Managed Postgres.
- Do not commit secrets; use Render environment variables.
- If app fails to start, check Render service logs for the JVM output and container stdout.

If you want, I can also create a Render `service` YAML or help set the exact env values in your Render dashboard.
