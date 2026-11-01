#!/usr/bin/env python3
"""Create a source archive for GitHub Releases, outside the public website."""
from pathlib import Path
import subprocess
import zipfile

root = Path(__file__).resolve().parents[1]
target = root / 'build/distributions/makallete-tv-launcher-source.zip'
target.parent.mkdir(parents=True, exist_ok=True)
if (root / '.git').exists():
    paths = subprocess.check_output(['git', '-C', str(root), 'ls-files', '-z']).decode().split('\0')
    files = [root / path for path in paths if path]
else:
    # An extracted source ZIP has no Git metadata. Only known source directories are allowed.
    folders = ['app/src', 'docs', 'gradle/wrapper', 'site', 'scripts', '.github']
    files = [file for folder in folders for file in (root / folder).rglob('*') if file.is_file()]
    files += [file for file in root.iterdir() if file.is_file() and
              (file.name in {'LICENSE', '.gitignore', '.gitattributes', 'gradlew', 'gradlew.bat', 'netlify.toml'} or
               file.suffix in {'.md', '.kts'} or file.name == 'gradle.properties')]
    files += [root / 'app/build.gradle.kts', root / '.impeccable/design.json']
forbidden = {'.git', '.gradle', '.kotlin', 'build', '.netlify', '.idea', '__pycache__', 'private'}
files = sorted(set(file for file in files if file.is_file() and file != target and
                   not forbidden.intersection(file.relative_to(root).parts) and
                   file.suffix not in {'.apk', '.aab', '.jks', '.keystore', '.pem', '.key', '.zip'} and
                   file.name != 'local.properties' and not file.name.startswith('.env')))
with zipfile.ZipFile(target, 'w', zipfile.ZIP_DEFLATED) as bundle:
    for file in files:
        name = 'makallete-tv-launcher/' + file.relative_to(root).as_posix()
        info = zipfile.ZipInfo(name, (2026, 1, 1, 0, 0, 0))
        info.external_attr = (0o100755 if file.name == 'gradlew' else 0o100644) << 16
        info.compress_type = zipfile.ZIP_DEFLATED
        bundle.writestr(info, file.read_bytes())
print(f'Created {target.name}: {len(files)} source files; no build outputs or private signing files.')
