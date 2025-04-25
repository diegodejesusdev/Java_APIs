# Script to safely install Maven on systems with special characters in paths
$ErrorActionPreference = "Stop"

# Function to read a properties file safely
function Read-PropertiesFile {
    param (
        [string]$Path
    )
    
    $properties = @{}
    
    if (Test-Path $Path) {
        Get-Content $Path | ForEach-Object {
            $line = $_.Trim()
            if ($line -and !$line.StartsWith("#")) {
                $key, $value = $line -split '=', 2
                if ($key -and $value) {
                    $properties[$key.Trim()] = $value.Trim()
                }
            }
        }
    }
    else {
        Write-Error "Properties file not found: $Path"
    }
    
    return $properties
}

Write-Host "Installing Maven..." -ForegroundColor Cyan

# Get current script directory safely
$scriptDir = $PSScriptRoot
Write-Host "Script directory: $scriptDir"

# Read the maven-wrapper.properties file
$propertiesPath = Join-Path -Path $scriptDir -ChildPath ".mvn\wrapper\maven-wrapper.properties"
Write-Host "Reading properties from: $propertiesPath"

$properties = Read-PropertiesFile -Path $propertiesPath
$distributionUrl = $properties["distributionUrl"]

if (-not $distributionUrl) {
    Write-Error "distributionUrl not found in maven-wrapper.properties"
    exit 1
}

Write-Host "Maven distribution URL: $distributionUrl"

# Determine Maven home directory
$distributionUrlName = $distributionUrl -replace '^.*/', ''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$', '' -replace '-bin$', ''
$mavenHomeParent = Join-Path -Path $env:USERPROFILE -ChildPath ".m2\wrapper\dists\$distributionUrlNameMain"

# Create a hash of the distribution URL for unique folder name
$md5 = [System.Security.Cryptography.MD5]::Create()
$urlBytes = [System.Text.Encoding]::UTF8.GetBytes($distributionUrl)
$hashBytes = $md5.ComputeHash($urlBytes)
$mavenHomeName = ($hashBytes | ForEach-Object { $_.ToString("x2") }) -join ''
$mavenHome = Join-Path -Path $mavenHomeParent -ChildPath $mavenHomeName

# Check if Maven is already installed
if (Test-Path -Path $mavenHome -PathType Container) {
    Write-Host "Maven already installed at: $mavenHome" -ForegroundColor Green
    $mavenBinPath = Join-Path -Path $mavenHome -ChildPath "bin\mvn.cmd"
    Write-Host "Maven binary path: $mavenBinPath"
    return $mavenBinPath
}

# Create temp directory for download
$tempDir = Join-Path -Path $env:TEMP -ChildPath ([System.Guid]::NewGuid().ToString())
New-Item -ItemType Directory -Path $tempDir -Force | Out-Null
Write-Host "Created temporary directory: $tempDir"

try {
    # Download Maven
    $downloadPath = Join-Path -Path $tempDir -ChildPath $distributionUrlName
    Write-Host "Downloading Maven to: $downloadPath"
    
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $webClient = New-Object System.Net.WebClient
    $webClient.DownloadFile($distributionUrl, $downloadPath)
    
    Write-Host "Download complete"
    
    # Create the Maven home directory
    New-Item -ItemType Directory -Path $mavenHomeParent -Force | Out-Null
    
    # Extract the archive
    Write-Host "Extracting Maven..."
    Expand-Archive -Path $downloadPath -DestinationPath $tempDir
    
    # Move to final location
    Write-Host "Moving Maven to permanent location..."
    $extractedDir = Join-Path -Path $tempDir -ChildPath $distributionUrlNameMain
    
    if (Test-Path $extractedDir) {
        Move-Item -Path $extractedDir -Destination $mavenHome
        Write-Host "Maven installed successfully at: $mavenHome" -ForegroundColor Green
        $mavenBinPath = Join-Path -Path $mavenHome -ChildPath "bin\mvn.cmd"
        Write-Host "Maven binary path: $mavenBinPath"
        return $mavenBinPath
    }
    else {
        Write-Error "Could not find extracted Maven directory: $extractedDir"
        exit 1
    }
}
catch {
    Write-Error "Error installing Maven: $_"
    exit 1
}
finally {
    # Clean up
    if (Test-Path $tempDir) {
        Write-Host "Cleaning up temporary files..."
        Remove-Item -Path $tempDir -Recurse -Force -ErrorAction SilentlyContinue
    }
}

