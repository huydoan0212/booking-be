# -- .\script\createMigration.ps1 -desc "create_table_users" -type "table"
# -- .\script\createMigration.ps1 -desc "init_data_users" -type "seed"

Param(
    [string]$desc = "default_migration",
    [string]$type = "table"
)

# Lấy timestamp hiện tại theo định dạng yyyyMMddHHmmss
$timestamp = Get-Date -Format "yyyyMMddHHmmss"

# Tạo tên file migration, ví dụ: V20250310123456__create_table_user.sql
$fileName = "V${timestamp}__${desc}.sql"

# Xác định thư mục chứa file migration dựa theo loại (table hoặc seed)
$targetDir = "src/main/resources/db/migration/$type"

# Nếu thư mục không tồn tại thì tạo mới
if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir | Out-Null
}

# Xác định đường dẫn file đầy đủ
$fullPath = Join-Path $targetDir $fileName

# Xác định nội dung file migration dựa trên loại
if ($type -eq "table") {
    $content = @'
CREATE TABLE "" (
    "id" uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    "created_at" timestamp with time zone NOT NULL DEFAULT now(),
    "updated_at" timestamp with time zone NOT NULL DEFAULT now()
);

CREATE TRIGGER update_modified_time BEFORE UPDATE ON ""
FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
'@
} elseif ($type -eq "seed") {
    $content = @'
INSERT INTO "" () VALUES ();
'@
} else {
    $content = "-- Write your migration SQL here"
}

# Ghi nội dung vào file sử dụng mã hóa UTF8
$content | Out-File -FilePath $fullPath -Encoding UTF8

Write-Output "Migration file created: $fullPath"
