DESCRIPTION = "pyhailort - hailo's python API \
		the recipe depends on _pyhailort shared object compiles in pyhailort-shared \
		the recipe installed using pyhailort setuptools into python/site-packages \
		the recipe contains all the python dependencies and it's currently supported by python 3.6 and 3.7"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://../../../../LICENSE;md5=48b1c947c88868c23e4fb874890be6fc \
                    file://../../../../LICENSE-3RD-PARTY.md;md5=330ab01bc49485a65979d8c28daee4b0"

SRC_URI = "git://git@github.com/hailo-ai/hailort.git;protocol=https;branch=develop"
SRCREV = "6aba2a23e509da3311694d5a7eab5c690335790c"

S = "${WORKDIR}/git/hailort/libhailort/bindings/python/platform"

DEPENDS = "python3 pyhailort-shared python3-wheel-native"

inherit setuptools3

RDEPENDS_${PN} += "libhailort python3-pyyaml python3-future python3-importlib-metadata python3-netifaces \
				   python3-six python3-appdirs python3-configparser python3-contextlib2 \
				   python3-netaddr python3-ply python3-protobuf python3-smmap \ 
				   python3-tqdm python3-argcomplete python3-verboselogs \
				   python3-aspy.yaml python3-cppheaderparser python3-zmq \ 
				   python3-identify python3-cfgv python3-numpy python3-pyzmq python3-setuptools"

PACKAGE_NAME = "hailo_platform"

PY_VER = "${@ '${PYTHON_BASEVERSION}'.replace('.', '')}"
M_FLAG = "${@ '' if '${PY_VER}' == '38' else 'm'}"

PYHAILORT_SHARED_FILE = "_pyhailort.cpython-${PY_VER}${M_FLAG}-${TARGET_ARCH}-linux-gnu.so"
PYHAILORT_SHARED_PATH = "${TMPDIR}/staging/lib"

# pyhailort shared object files are stripped, QA issue should be skipped
INSANE_SKIP_${PN} += "already-stripped"

# copy pyhailort shared object and hailort.h before configuration and compilation
do_configure_prepend() {
    cp ${PYHAILORT_SHARED_PATH}/${PYHAILORT_SHARED_FILE} ${S}/hailo_platform/pyhailort
    echo '{"py_version": "${PY_VER}", "arch": "${TARGET_ARCH}", "hrt_version": "${PV}"}' > ${S}/wheel_conf.json
}
