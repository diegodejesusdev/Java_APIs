# Script to safely run Maven on systems with special characters in paths

# Execute the Maven installer script
$mavenPath = & "$PSScriptRoot\install-maven.ps1"

if (-not $mavenPath -or -not (Test-Path $mavenPath)) {
    Write-Error "Failed to locate Maven binary"
    exit 1
}

Write-Host "Running Maven with arguments: $args" -ForegroundColor Cyan

# Execute Maven with all arguments passed to this script
try {
    & $mavenPath $args
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -eq 0) {
        Write-Host "Maven completed successfully" -ForegroundColor Green
    }
    else {
        Write-Host "Maven exited with code: $exitCode" -ForegroundColor Red
    }
    
    exit $exitCode
}
catch {
    Write-Error "Error executing Maven: $_"
    exit 1
}

