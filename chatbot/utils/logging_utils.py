import logging

def get_logger(name=__name__):
    logger = logging.getLogger(name)
    if not logger.hasHandlers():  # 중복 핸들러 방지
        handler = logging.StreamHandler()
        formatter = logging.Formatter('[%(asctime)s] %(levelname)s - %(message)s')
        handler.setFormatter(formatter)
        logger.addHandler(handler)
        logger.setLevel(logging.DEBUG)
    return logger
